import { VStack } from '@chakra-ui/react';

import { LoadingContainer } from '../../ui/loading-container/loading-container';
import { ReferenceToken } from '../reference-token/reference-token';

interface ReferenceListProps {
  isLoading: boolean;
  tokens?: any[];
}

export const ReferenceList = ({ isLoading, tokens }: ReferenceListProps) => {
  const error = !isLoading && !tokens ? 'Error loading reference data' : undefined;

  return (
    <LoadingContainer isLoading={isLoading} error={error} loadingProps={{ pt: '2rem' }}>
      {tokens && (
        <VStack spacing="0.5rem" align="stretch" p="0.5rem">
          {tokens.map((token: any) => (
            <ReferenceToken key={token.key} token={token} />
          ))}
        </VStack>
      )}
    </LoadingContainer>
  );
};
