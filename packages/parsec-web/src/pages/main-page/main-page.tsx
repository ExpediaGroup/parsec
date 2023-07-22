import { Flex } from '@chakra-ui/react';
import { useDispatch, useSelector } from 'react-redux';

import { ActivityBar } from '../../components/activity-bar/activity-bar';
import { enableResizable } from '../../shared/resizable-utils';
import { appSlice } from '../../store/app.slice';
import type { RootState } from '../../store/store';
import { EditorPage } from '../editor-page/editor-page';
import { Sidebar } from '../sidebar/sidebar';

export const MainPage = () => {
  const { isSidebarOpen } = useSelector((state: RootState) => state.app);
  const dispatch = useDispatch();

  return (
    <Flex
      direction={{ base: 'column', md: 'row' }}
      align="stretch"
      height={{ base: 'unset', md: '100vh' }}
      //width={{ base: '100vw', md: 'unset' }}
    >
      <ActivityBar />

      {isSidebarOpen && (
        <Sidebar
          
          defaultSize={{
            width: '20%',
            height: '100%'
          }}
          maxWidth="40%"
          enable={enableResizable({
            right: true
          })}
          onResize={(e, direction, ref, delta) => {
            // Snap closed
            if (ref.clientWidth < 100 && delta.width < 0) {
              dispatch(appSlice.actions.setSidebarOpen(false));
            }
          }}
          borderRight="2px solid"
          borderRightColor="gray.400"
          overflow="hidden"
        />
      )}

      <EditorPage flexGrow={1} />

      {/* <Footer /> */}
    </Flex>
  );
};
