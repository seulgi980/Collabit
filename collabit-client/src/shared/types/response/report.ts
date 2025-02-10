export interface Skill {
  name: string;
  score: number;
  description?: string;
  feedback?: string;
  positive?: boolean;
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

export type ChartResponse = {
  hexagon: ChartRangeData & SkillData;
  progress: ChartRangeData & SkillData;
};

export type WordWeight = {
  text: string;
  weight: number;
};

export type WordCloudResponse = {
  positive: WordWeight[];
  negative: WordWeight[];
};

export type AISummaryResponse = {
  positive: string;
  negative: string;
};

export type Timeline = SkillData & {
  projectName: string;
  organization: string;
  completedAt: Date;
};

export type TimelineResponse = ChartRangeData & {
  timeline: Timeline[];
};
