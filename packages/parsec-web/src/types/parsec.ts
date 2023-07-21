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

export type ReferenceToken = {
  key: string;
  typekey: string;
  subtypekey: string;
  name: string;
  altName?: string;
  description: any;
  type: TokenType;
  subtype: any;
  syntax: any;
  examples?: any[];
  aliases?: any[];
  related?: any[];
};

export type TokenType = 'function' | 'input' | 'literal' | 'operator' | 'statement' | 'symbol';
