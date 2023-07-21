import type { PayloadAction } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';

import type { TokenType } from '../types/parsec';

export interface ReferenceState {
  search: string;
  tokenType?: TokenType;
}

const initialState: ReferenceState = {
  search: '',
  tokenType: undefined
};

export const referenceSlice = createSlice({
  name: 'reference',
  initialState,
  reducers: {
    setSearch(state, action: PayloadAction<string>) {
      state.search = action.payload;
    },
    clearSearch(state) {
      state.search = '';
      state.tokenType = undefined;
    },
    setTokenType(state, action: PayloadAction<TokenType>) {
      state.tokenType = state.tokenType === action.payload ? undefined : action.payload;
    },
    clearTokenType(state) {
      state.tokenType = undefined;
    }
  }
});
