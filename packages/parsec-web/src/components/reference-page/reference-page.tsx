import { Alert, AlertDescription, AlertIcon, Heading, HStack, Spinner, Text, VStack } from '@chakra-ui/react';
import { useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';

import { Link } from '../../components/link/link';
import { ReferenceDetail } from '../../components/reference-detail/reference-detail';
import { TokenIcon } from '../../components/token-icon/token-icon';
import { useExecuteQueryMutation } from '../../store/parsec.slice';

export const ReferencePage = () => {
  const [executeQuery, { data, isLoading }] = useExecuteQueryMutation();

  useEffect(() => {
    executeQuery({ query: 'input docs' });
  }, []);

  const tokens = data?.dataSets[0].data;

  return (
    <VStack spacing="1rem" align="stretch" overflow="auto">
      <Heading as="h2" textStyle="parsec">
        Reference
      </Heading>

      <Routes>
        <Route
          path="/"
          element={
            <>
              {isLoading && (
                <HStack justify="center" pt="2rem">
                  <Spinner
                    thickness="3px"
                    speed="0.65s"
                    emptyColor="gray.200"
                    color="parsec.pink"
                    size="xl"
                    mr="0.5rem"
                  />
                </HStack>
              )}
              {!isLoading && !data && (
                <Alert status="error">
                  <AlertIcon />
                  <AlertDescription>Error loading reference data</AlertDescription>
                </Alert>
              )}
              {tokens && (
                <VStack spacing="0.5rem" align="stretch" p="0.5rem">
                  {tokens.map((row: any) => (
                    <Link to={row.name} key={row.name + ':' + row.altName}>
                      <HStack spacing="0.5rem" align="center">
                        <TokenIcon type={row.type} />
                        <Text p={0} m={0}>
                          {row.name} {row.altName && <span>({row.altName})</span>}
                        </Text>
                      </HStack>
                    </Link>
                  ))}
                </VStack>
              )}
            </>
          }
        />
        <Route path="/:slug" element={<ReferenceDetail tokens={tokens} />}></Route>
      </Routes>
    </VStack>
  );
};
