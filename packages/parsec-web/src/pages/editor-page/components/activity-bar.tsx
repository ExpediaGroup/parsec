import { useColorModeValue, Stack } from '@chakra-ui/react';
import { Flex, IconButton } from '@chakra-ui/react';
import type { Dispatch } from '@reduxjs/toolkit';
import { useDispatch, useSelector } from 'react-redux';

import type { IconFactoryKeys } from '../../../shared/icon-factory';
import { iconFactoryAs } from '../../../shared/icon-factory';
import { Activity } from '../../../store/editor.slice';
import { editorSlice } from '../../../store/editor.slice';
import type { RootState } from '../../../store/store';

const ActivityButton = ({
  activity,
  icon,
  tooltip
}: {
  activity: Activity;
  icon: IconFactoryKeys;
  tooltip: string;
}) => {
  const dispatch = useDispatch();
  const { currentActivity, isSidebarOpen } = useSelector((state: RootState) => state.editor);
  const activeColor = useColorModeValue('parsec.purple', 'parsec.pink');

  const isCurrentActivity = currentActivity === activity;

  const selectActivity = (activity: Activity) => {
    if (currentActivity === activity) {
      dispatch(editorSlice.actions.toggleSidebar());
    } else {
      dispatch(editorSlice.actions.setCurrentActivity(activity));
      dispatch(editorSlice.actions.setSidebarOpen(true));
    }
  };

  return (
    <IconButton
      icon={iconFactoryAs(icon)}
      onClick={() => selectActivity(activity)}
      aria-label={tooltip}
      boxSize="3rem"
      fontSize="1.25rem"
      variant="ghost"
      bg={isCurrentActivity && isSidebarOpen ? activeColor : undefined}
      color={isCurrentActivity && isSidebarOpen ? 'white' : undefined}
    />
  );
};

export const ActivityBar = () => {
  return (
    <Stack
      direction={{ base: 'row', md: 'column' }}
      spacing="0.25rem"
      p="0.25rem"
      bg={useColorModeValue('gray.200', 'gray.900')}
    >
      <ActivityButton activity={Activity.Queries} icon="tree" tooltip="Queries" />

      <ActivityButton activity={Activity.Settings} icon="settings" tooltip="Settings" />
    </Stack>
  );
};
