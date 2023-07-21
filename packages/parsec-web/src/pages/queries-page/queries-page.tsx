import { Heading, VStack } from '@chakra-ui/react';

export const QueriesPage = () => {
  return (
    <VStack spacing="1rem" align="stretch" overflow="auto" flexGrow={1} p="0.25rem">
      <Heading as="h2" textStyle="parsec">
        Queries
      </Heading>
    </VStack>
  );
};
