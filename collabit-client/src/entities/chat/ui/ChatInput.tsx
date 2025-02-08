import { Button } from "@/shared/ui/button";
import { Textarea } from "@/shared/ui/textarea";
import { SendIcon } from "lucide-react";

interface ChatInputProps {
  message: string;
  setMessage: (message: string) => void;
  handleSendMessage: (e: React.FormEvent<HTMLFormElement>) => void;
}

const ChatInput = ({
  message,
  setMessage,
  handleSendMessage,
}: ChatInputProps) => {
  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (
      e.key === "Enter" &&
      !e.shiftKey &&
      e.nativeEvent.isComposing === false
    ) {
      e.preventDefault();
      if (message.trim() !== "") {
        handleSendMessage(e as unknown as React.FormEvent<HTMLFormElement>);
      }
    }
  };

  return (
    <form className="flex items-center gap-2" onSubmit={handleSendMessage}>
      <Textarea
        placeholder="메시지를 입력하세요"
        className="max-h-[200px] min-h-9 w-full resize-none bg-white text-sm md:text-base"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onKeyDown={handleKeyDown}
        rows={1}
      />
      <Button type="submit" className="flex items-center justify-center">
        <SendIcon className="h-full w-full" />
      </Button>
    </form>
  );
};

export default ChatInput;
