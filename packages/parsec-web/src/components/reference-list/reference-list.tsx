import { VStack } from '@chakra-ui/react';

import { LoadingContainer } from '../../ui/loading-container/loading-container';
import { ReferenceToken } from '../reference-token/reference-token';

import { SearchBar } from './search-bar/search-bar';

interface ReferenceListProps {
  isLoading: boolean;
  tokens?: any[];
}

export const ReferenceList = ({ isLoading, tokens }: ReferenceListProps) => {
  const error = !isLoading && !tokens ? 'Error loading reference data' : undefined;

  return (
    <LoadingContainer isLoading={isLoading} error={error} loadingProps={{ pt: '2rem' }}>
      <VStack spacing="2rem" align="stretch" p="0.5rem">
        <SearchBar />
        <VStack spacing="0.5rem" align="stretch">
          {tokens?.map((token: any) => (
            <ReferenceToken key={token.key} token={token} />
          ))}
        </VStack>
      </VStack>
    </LoadingContainer>
  );
};
