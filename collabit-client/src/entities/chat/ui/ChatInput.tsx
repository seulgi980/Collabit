import { Button } from "@/shared/ui/button";
import { Input } from "@/shared/ui/input";
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
  return (
    <form className="flex items-center gap-2" onSubmit={handleSendMessage}>
      <Input
        placeholder="메시지를 입력하세요"
        className="w-full bg-white"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
      />
      <Button type="submit" className="flex items-center justify-center">
        <SendIcon className="h-full w-full" />
      </Button>
    </form>
  );
};

export default ChatInput;
