class NotificationService {
  private static instance: NotificationService;
  private eventSource: EventSource | null = null;
  private subscribers: Set<(data: any) => void> = new Set();

  private constructor() {}

  static getInstance() {
    if (!this.instance) {
      this.instance = new NotificationService();
    }
    return this.instance;
  }

  connect() {
    if (this.eventSource?.readyState === EventSource.OPEN) return;

    if (this.eventSource) {
      this.eventSource.close();
    }

    this.eventSource = new EventSource(
      `${process.env.NEXT_PUBLIC_API_URL}/sse/subscribe`,
      {
        withCredentials: true,
      },
    );

    this.eventSource.onmessage = (event) => {
      const data = JSON.parse(event.data);
      this.subscribers.forEach((callback) => callback(data));
      console.log("data", data);
    };

    this.eventSource.onerror = (error) => {
      console.error("EventSource failed:", error);
      this.reconnect();
    };
  }

  private reconnect() {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }

    // 3초 후 재연결 시도
    setTimeout(() => this.connect(), 3000);
  }

  disconnect() {
    this.eventSource?.close();
    this.eventSource = null;
  }

  subscribe(callback: (data: any) => void) {
    this.subscribers.add(callback);
    return () => this.subscribers.delete(callback);
  }
}

export const notificationService = NotificationService.getInstance();
