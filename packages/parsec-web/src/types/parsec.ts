export interface ExecutionResultDataSet {
  name: string;
  temporary: boolean;
  count: number;
  columns: string[];
  data: any[];
}

export interface ExecutionResult {
  parsedTree: any;
  prettyParsedTree: string;
  memory: {
    free: number;
    total: number;
    max: number;
  };
  performance: {
    parse: number;
    compile: number;
    execute: number;
    total: number;
  };
  errors: any[];
  dataSets: ExecutionResultDataSet[];
}
