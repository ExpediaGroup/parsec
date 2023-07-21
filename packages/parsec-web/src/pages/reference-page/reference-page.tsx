import { Heading, VStack } from '@chakra-ui/react';
import MiniSearch from 'minisearch';
import { useEffect, useMemo } from 'react';
import { useSelector } from 'react-redux';
import { Routes, Route } from 'react-router-dom';

import { ReferenceDetail } from '../../components/reference-detail/reference-detail';
import { ReferenceList } from '../../components/reference-list/reference-list';
import { useExecuteQueryMutation } from '../../store/parsec.slice';
import type { RootState } from '../../store/store';
import type { ReferenceToken } from '../../types/parsec';

export const ReferencePage = () => {
  const { search, tokenType } = useSelector((state: RootState) => state.reference);

  const minisearch = useMemo(
    () =>
      new MiniSearch({
        idField: 'key',
        fields: [
          'key',
          'name',
          'altName',
          'description',
          'type',
          'subtype',
          'syntax',
          'examples',
          'aliases',
          'related'
        ],
        storeFields: ['type', 'subtype'],
        extractField: (document, fieldName) => {
          if (Array.isArray(document[fieldName])) {
            return JSON.stringify(document[fieldName]);
          }

          return document[fieldName];
        }
      }),
    []
  );

  const [executeQuery, { data, isLoading }] = useExecuteQueryMutation();

  useEffect(() => {
    const initializeMiniSearch = async () => {
      const data = await executeQuery({ query: 'input docs' }).unwrap();
      minisearch.addAll(data.dataSets[0].data);
    };

    initializeMiniSearch();
  }, []);

  const tokens = useMemo(() => {
    if (!data) {
      return [];
    }

    const newTokens = [...data.dataSets[0].data];

    if (newTokens) {
      // Sort alphabetically
      newTokens.sort((a: ReferenceToken, b: ReferenceToken) => a.name.localeCompare(b.name));
    }

    return newTokens;
  }, [data]);

  const filteredTokens: any = useMemo(() => {
    if (!tokens) {
      return [];
    }

    let filtered;

    if (search === '') {
      filtered = tokens;
    } else {
      // Search for matching tokens
      const results = minisearch.search(search, {
        prefix: true,
        fuzzy: 0.2,
        filter: (result) => {
          if (search.startsWith('type:')) {
            return result.type === search.slice(5);
          } else if (search.startsWith('subtype:')) {
            return result.subtype === search.slice(8);
          }

          return true;
        }
      });
      console.log(results);
      filtered = results.map((result) => tokens.find((t: ReferenceToken) => t.key === result.id));
    }

    // Filter by token type (if specified)
    if (tokenType) {
      filtered = filtered.filter((token: ReferenceToken) => token.type === tokenType);
    }

    return filtered;
  }, [tokens, search, tokenType]);

  return (
    <VStack spacing="1rem" align="stretch" overflow="auto" flexGrow={1} p="0.25rem">
      <Heading as="h2" textStyle="parsec">
        Reference
      </Heading>

      <Routes>
        <Route path="/" element={<ReferenceList isLoading={isLoading} tokens={filteredTokens} />} />
        <Route path="/:key" element={<ReferenceDetail tokens={filteredTokens} />} />
      </Routes>
    </VStack>
  );
};
