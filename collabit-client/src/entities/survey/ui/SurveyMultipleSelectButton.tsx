import { useSurveyStore } from "@/shared/lib/stores/surveyStore";
import ImojiButton from "@/shared/ui/ImojiButton";
import { useState } from "react";

interface SurveyMultipleSelectButtonProps {
  index: number;
  onClick: () => void;
}

const SurveyMultipleSelectButton = ({
  index,
  onClick,
}: SurveyMultipleSelectButtonProps) => {
  const [selectedValue, setSelectedValue] = useState<number | null>(null);
  const setScores = useSurveyStore((state) => state.setScores);

  const handleClick = (value: number) => {
    console.log(value);
    setScores(value, index);
    setSelectedValue(value);
    onClick();
  };

  return (
    <div
      className="flex gap-5 text-sm md:text-lg"
      role="radiogroup"
      aria-label="만족도 선택"
    >
      {[
        { value: 1, emoji: "😔", label: "매우 불만족" },
        { value: 2, emoji: "🙁", label: "불만족" },
        { value: 3, emoji: "🙂", label: "보통" },
        { value: 4, emoji: "😀", label: "만족" },
        { value: 5, emoji: "😆", label: "매우 만족" },
      ].map(({ value, emoji, label }) => (
        <ImojiButton
          key={value}
          isSelected={selectedValue === value}
          onClick={() => handleClick(value)}
          role="radio"
          aria-checked={selectedValue === value}
          aria-label={label}
        >
          {emoji}
        </ImojiButton>
      ))}
    </div>
  );
};
export default SurveyMultipleSelectButton;
