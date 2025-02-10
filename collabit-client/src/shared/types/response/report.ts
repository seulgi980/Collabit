export interface Skill {
  name: string;
  score: number;
  description?: string;
  feedback?: string;
  isPositive?: boolean;
}

export interface ChartRangeData {
  minScore: number;
  maxScore: number;
}

export interface SkillData {
  sympathy: Skill;
  listening: Skill;
  expression: Skill;
  problemSolving: Skill;
  conflictResolution: Skill;
  leadership: Skill;
}

export type ChartResponse = ChartRangeData & {
  hexagonData: SkillData;
};
