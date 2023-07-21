import { Input, InputGroup, InputLeftElement, InputRightElement } from '@chakra-ui/react';

import { iconFactoryAs } from '../../../../shared/icon-factory';
import { IconButton } from '../../../../ui/icon-button/icon-button';

export interface SearchBoxProps {
  onClear: () => void;
  onQueryChange: (query: string) => void;
  query: string;
}

export const SearchBox = ({ onClear, onQueryChange, query }: SearchBoxProps) => {
  return (
    <InputGroup>
      <InputLeftElement pointerEvents="none" children={iconFactoryAs('search')} />
      <Input placeholder="Search" variant="flushed" onChange={(e) => onQueryChange(e.target.value)} value={query} />
      <InputRightElement>
        <IconButton icon="close" variant="ghost" tooltip="Clear search" aria-label="Clear search" onClick={onClear} />
      </InputRightElement>
    </InputGroup>
  );
};
