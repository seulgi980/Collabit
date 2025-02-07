import ImojiButton from "@/shared/ui/ImojiButton";
import { useState } from "react";

const SurveyMultipleSelectButton = ({}) => {
  const [selectedValue, setSelectedValue] = useState<number | null>(null);

  return (
    <div className="flex gap-5 text-sm md:text-lg">
      <ImojiButton
        value={1}
        isSelected={selectedValue === 1}
        onClick={() => setSelectedValue(1)}
      >
        😔
      </ImojiButton>
      <ImojiButton
        value={2}
        isSelected={selectedValue === 2}
        onClick={() => setSelectedValue(2)}
      >
        🙁
      </ImojiButton>
      <ImojiButton
        value={3}
        isSelected={selectedValue === 3}
        onClick={() => setSelectedValue(3)}
      >
        🙂
      </ImojiButton>
      <ImojiButton
        value={4}
        isSelected={selectedValue === 4}
        onClick={() => setSelectedValue(4)}
      >
        😀
      </ImojiButton>
      <ImojiButton
        value={5}
        isSelected={selectedValue === 5}
        onClick={() => setSelectedValue(5)}
      >
        😆
      </ImojiButton>
    </div>
  );
};
export default SurveyMultipleSelectButton;
