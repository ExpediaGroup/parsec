import { Heading, VStack } from '@chakra-ui/react';
import { useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';

import { ReferenceDetail } from '../../components/reference-detail/reference-detail';
import { ReferenceList } from '../../components/reference-list/reference-list';
import { useExecuteQueryMutation } from '../../store/parsec.slice';

export const ReferencePage = () => {
  const [executeQuery, { data, isLoading }] = useExecuteQueryMutation();

  useEffect(() => {
    executeQuery({ query: 'input docs' });
  }, []);

  const tokens = data?.dataSets[0].data;

  return (
    <VStack spacing="1rem" align="stretch" overflow="auto" flexGrow={1} p="0.25rem">
      <Heading as="h2" textStyle="parsec">
        Reference
      </Heading>

      <Routes>
        <Route path="/" element={<ReferenceList isLoading={isLoading} tokens={tokens} />} />
        <Route path="/:key" element={<ReferenceDetail tokens={tokens} />} />
      </Routes>
    </VStack>
  );
};
