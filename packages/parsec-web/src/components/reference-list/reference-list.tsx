import { Alert, AlertDescription, AlertIcon, HStack, Spinner, VStack } from '@chakra-ui/react';

import { ReferenceToken } from '../reference-token/reference-token';

interface ReferenceListProps {
  isLoading: boolean;
  tokens?: any[];
}

export const ReferenceList = ({ isLoading, tokens }: ReferenceListProps) => {
  return (
    <>
      {isLoading && (
        <HStack justify="center" pt="2rem">
          <Spinner thickness="3px" speed="0.65s" emptyColor="gray.200" color="parsec.pink" size="xl" mr="0.5rem" />
        </HStack>
      )}
      {!isLoading && !tokens && (
        <Alert status="error">
          <AlertIcon />
          <AlertDescription>Error loading reference data</AlertDescription>
        </Alert>
      )}
      {tokens && (
        <VStack spacing="0.5rem" align="stretch" p="0.5rem">
          {tokens.map((token: any) => (
            <ReferenceToken key={token.key} token={token} />
          ))}
        </VStack>
      )}
    </>
  );
};
