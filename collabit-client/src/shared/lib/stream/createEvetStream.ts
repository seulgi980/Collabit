const createEventStream = (response: Response) => {
  const reader = response.body?.getReader();
  if (!reader) {
    throw new Error("스트림 리더를 생성할 수 없습니다");
  }

  return {
    stream: new ReadableStream({
      async start(controller) {
        try {
          while (true) {
            const { done, value } = await reader.read();
            if (done) {
              break;
            }
            const text = new TextDecoder().decode(value);
            const data = text
              .split("\n")
              .filter((line) => line.startsWith("data: "))
              .map((line) => JSON.parse(line.slice(6)));

            data.forEach((item) => controller.enqueue(item));
          }
        } catch (error) {
          controller.error(error);
        } finally {
          controller.close();
          reader.releaseLock();
        }
      },
    }),
  };
};
export default createEventStream;
