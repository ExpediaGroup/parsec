import { IconButton, Image, Stack, useColorMode, useColorModeValue } from '@chakra-ui/react';
import { useDispatch, useSelector } from 'react-redux';

import parsecLogoUrl from '../../../assets/parsec-logo.svg';
import type { IconFactoryKeys } from '../../shared/icon-factory';
import { iconFactoryAs } from '../../shared/icon-factory';
import { Activity, appSlice } from '../../store/app.slice';
import type { RootState } from '../../store/store';
import { Link } from '../../ui/link/link';

import { Stripes } from './stripes';

const ActivityButton = ({
  activity,
  icon,
  to,
  tooltip
}: {
  activity: Activity;
  icon: IconFactoryKeys;
  to: string;
  tooltip: string;
}) => {
  const dispatch = useDispatch();
  const { currentActivity, isSidebarOpen } = useSelector((state: RootState) => state.app);
  const activeColor = useColorModeValue('parsec.pink', 'parsec.pink');
  const currentColor = useColorModeValue('unset', 'white');

  const isCurrentActivity = currentActivity === activity;

  const selectActivity = (activity: Activity) => {
    if (activity === Activity.Home) {
      // Always dispatch the home activity to trigger the side effect that selects the home tab
      dispatch(appSlice.actions.setCurrentActivity(activity));
    } else if (currentActivity === activity) {
      dispatch(appSlice.actions.toggleSidebar());
    } else {
      dispatch(appSlice.actions.setCurrentActivity(activity));
      dispatch(appSlice.actions.setSidebarOpen(true));
    }
  };

  return (
    <Link to={to}>
      <IconButton
        icon={iconFactoryAs(icon)}
        onClick={() => selectActivity(activity)}
        aria-label={tooltip}
        boxSize="3rem"
        fontSize="1.25rem"
        variant="ghost"
        bg={isCurrentActivity && isSidebarOpen ? activeColor : undefined}
        color={isCurrentActivity && isSidebarOpen ? currentColor : undefined}
        _hover={{
          bg: 'transparent',
          color: 'parsec.pink'
        }}
      />
    </Link>
  );
};

export const ActivityBar = () => {
  const { colorMode, toggleColorMode } = useColorMode();

  return (
    <Stack
      direction={{ base: 'row', md: 'column' }}
      spacing="0.25rem"
      justify="space-between"
      align="center"
      pt="0.25rem"
      pb={{ base: '0.25rem', md: 0 }}
      px={{ base: '0.25rem', md: 0 }}
      bg={useColorModeValue('gray.200', 'gray.900')}
    >
      <Stack direction={{ base: 'row', md: 'column' }} spacing="0.25rem">
        <Image src={parsecLogoUrl} boxSize="2rem" m="0.5rem" />

        <ActivityButton to="/" activity={Activity.Home} icon="home" tooltip="Home" />

        <ActivityButton to="/queries" activity={Activity.Queries} icon="queries" tooltip="Queries" />

        <ActivityButton to="/history" activity={Activity.History} icon="history" tooltip="History" />

        <ActivityButton to="/reference" activity={Activity.Reference} icon="reference" tooltip="Reference" />
      </Stack>

      <Stack
        direction={{ base: 'row', md: 'column' }}
        spacing="0.25rem"
        align="center"
        width={{ md: '3.5rem' }}
        pb={{ base: 0, md: '0.25rem' }}
        overflow={{ md: 'hidden' }}
      >
        <Stripes display={{ base: 'none', md: 'flex' }} my="3rem" />

        <IconButton
          icon={iconFactoryAs(`${colorMode == 'light' ? 'dark' : 'light'}Mode`)}
          onClick={toggleColorMode}
          aria-label="Toggle color mode"
          boxSize="3rem"
          fontSize="1.25rem"
          variant="ghost"
          _hover={{
            bg: 'transparent',
            color: 'parsec.pink'
          }}
        />

        <ActivityButton to="/settings" activity={Activity.Settings} icon="settings" tooltip="Settings" />
      </Stack>
    </Stack>
  );
};
