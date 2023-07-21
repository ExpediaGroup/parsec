import { VStack } from '@chakra-ui/react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useDebouncedCallback } from 'use-debounce';

import { referenceSlice } from '../../../store/reference.slice';
import type { AppDispatch, RootState } from '../../../store/store';
import type { TokenType } from '../../../types/parsec';

import { SearchBox } from './search-box/search-box';
import { TokenTypeFilter } from './token-type-filter/token-type-filter';

export const SearchBar = () => {
  const dispatch = useDispatch<AppDispatch>();

  const { search, tokenType } = useSelector((state: RootState) => state.reference);

  const [internalSearch, setInternalSearch] = useState<string>(search);

  const debouncedDispatch = useDebouncedCallback(() => {
    dispatch(referenceSlice.actions.setSearch(internalSearch));
  }, 200);

  const onQueryChange = (query: string) => {
    setInternalSearch(query);
    debouncedDispatch();
  };

  const onClear = () => {
    dispatch(referenceSlice.actions.clearSearch());
    setInternalSearch('');
  };

  const onTokenType = (newTokenType: TokenType) => {
    dispatch(referenceSlice.actions.setTokenType(newTokenType));
  };

  return (
    <VStack spacing="0.5rem" align="stretch">
      <SearchBox query={internalSearch} onClear={onClear} onQueryChange={onQueryChange} />
      <TokenTypeFilter onTokenType={onTokenType} tokenType={tokenType} />
    </VStack>
  );
};
