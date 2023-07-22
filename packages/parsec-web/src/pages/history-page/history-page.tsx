import { Heading, HStack, MenuItem, Text, VStack } from '@chakra-ui/react';
import { useDispatch, useSelector } from 'react-redux';

import { historySlice } from '../../store/history.slice';
import type { RootState } from '../../store/store';
import { Card } from '../../ui/card/card';
import { IconButtonMenu } from '../../ui/icon-button-menu/icon-button-menu';

export const HistoryPage = () => {
  const dispatch = useDispatch();
  const { history } = useSelector((state: RootState) => state.history);

  const clearHistory = () => {
    dispatch(historySlice.actions.clearHistory());
  };

  return (
    <VStack spacing="1rem" align="stretch" overflow="auto" flexGrow={1} p="0.25rem">
      <HStack justify="space-between">
        <Heading as="h2" textStyle="parsec">
          History
        </Heading>
        <IconButtonMenu icon="menu" aria-label="Additional options" tooltip="Additional options">
          <MenuItem onClick={clearHistory}>Clear History</MenuItem>
        </IconButtonMenu>
      </HStack>

      <VStack spacing="0.5rem" align="stretch">
        {history.map((history) => (
          <Card key={history.id}>
            <Text noOfLines={1}>{history.query}</Text>
          </Card>
        ))}
      </VStack>
    </VStack>
  );
};
