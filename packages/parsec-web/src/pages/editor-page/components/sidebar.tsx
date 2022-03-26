import type { FlexProps } from '@chakra-ui/react';
import { Box, Flex } from '@chakra-ui/react';
import { useSelector } from 'react-redux';

import { Activity } from '../../../store/editor.slice';
import type { RootState } from '../../../store/store';

export const Sidebar = ({ ...flexProps }: FlexProps) => {
  const { currentActivity, isSidebarOpen } = useSelector((state: RootState) => state.editor);

  return (
    <Flex flexDirection="column" overflow="auto" {...flexProps} {...(!isSidebarOpen && { display: 'none' })}>
      {currentActivity === Activity.Queries && <Box>Queries</Box>}

      {currentActivity === Activity.Settings && <Box>Settings</Box>}
    </Flex>
  );
};
