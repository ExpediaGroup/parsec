import type { PayloadAction } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';

import type { ExecutionResult, ExecutionResultDataSet } from '../types/parsec';

export type HistoryResult = {
  id: string;
  query: string;
  status: 'success' | 'error' | 'running';
  executionResult?: ExecutionResult;
};

export interface HistoryState {
  history: HistoryResult[];
  results: Record<string, ExecutionResultDataSet[]>;
}

const initialState: HistoryState = {
  history: [],
  results: {}
};

export const historySlice = createSlice({
  name: 'history',
  initialState,
  reducers: {
    addHistory(state, action: PayloadAction<HistoryResult>) {
      state.history.unshift(action.payload);

      // Only keep the last 100 history items
      if (state.history.length > 100) {
        const removed = state.history.pop();
        delete state.results[removed!.id];
      }
    },
    updateHistory(state, action: PayloadAction<Required<HistoryResult>>) {
      const { id, executionResult, ...rest } = action.payload;
      const history = state.history.find((h) => h.id === id);
      if (history) {
        const { dataSets, ...limitedExecutionResult } = executionResult;

        history.executionResult = limitedExecutionResult as ExecutionResult;
        Object.assign(history, rest);
        state.results[id] = dataSets;
      }
    },
    clearHistory(state) {
      state.history = [];
      state.results = {};
    }
  }
});
