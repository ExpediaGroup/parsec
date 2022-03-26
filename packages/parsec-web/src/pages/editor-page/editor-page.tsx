import { Box, Flex, Icon, Stack, Tab, TabList, TabPanel, TabPanels, Tabs, useColorModeValue } from '@chakra-ui/react';
import { useDispatch, useSelector } from 'react-redux';

import { EditorTab } from '../../components/editor-tab/editor-tab';
import { iconFactory } from '../../shared/icon-factory';
import { editorSlice } from '../../store/editor.slice';
import type { RootState } from '../../store/store';

import { ActivityBar } from './components/activity-bar';
import { ClosableTab } from './components/closeable-tab';
import { Sidebar } from './components/sidebar';

export const EditorPage = () => {
  const dispatch = useDispatch();
  const { tabs, selectedTabIndex } = useSelector((state: RootState) => state.editor);

  const onTabChange = (index: number) => {
    dispatch(editorSlice.actions.setSelectedTab(index));
  };

  const onLabelChange = (tabKey: string, label: string) => {
    dispatch(editorSlice.actions.updateTabLabel({ tabKey, label }));
  };

  const onQueryChange = (tabKey: string, query: string) => {
    dispatch(editorSlice.actions.updateTabQuery({ tabKey, query }));
  };

  const onNewTab = () => {
    dispatch(editorSlice.actions.openNewTab());
  };

  const onCloseTab = (tabKey: string) => {
    dispatch(editorSlice.actions.closeTab({ tabKey }));
  };

  return (
    // Fill remaining space in page between header/footer
    // This fixes footer at the bottom of the screen
    <Flex direction="row" flexGrow={1} overflow="hidden" position="relative">
      {/* Create a scrolling container inside the page */}
      <Box position="absolute" top={0} bottom={0} left={0} right={0} overflow="hidden">
        <Stack direction={{ base: 'column', md: 'row' }} align="stretch" height="100%">
          <ActivityBar />
          <Sidebar flex="1 1 20%" maxWidth="28rem" />

          <Tabs
            display="flex"
            flexDirection="column"
            index={selectedTabIndex}
            onChange={onTabChange}
            flex="3 1 50%"
            pt="0.25rem"
            overflowX="hidden"
          >
            <TabList
              overflowY="hidden"
              sx={{
                scrollbarWidth: 'thin'
              }}
            >
              {tabs.map((tab) => (
                <ClosableTab key={tab.tabKey} onClose={() => onCloseTab(tab.tabKey)}>
                  {tab.label}
                </ClosableTab>
              ))}
              <Tab onClick={onNewTab}>
                <Icon
                  as={iconFactory('plus')}
                  color={useColorModeValue('parsec.pink', 'parsec.blue')}
                  _hover={{
                    color: useColorModeValue('parsec.blue', 'parsec.pink')
                  }}
                />
              </Tab>
            </TabList>
            <TabPanels display="flex" overflow="hidden" flexGrow={1}>
              {tabs.map((tab) => (
                <TabPanel key={tab.tabKey} flexGrow={1} overflow="auto">
                  <EditorTab
                    tab={tab}
                    query={tab.query}
                    onLabelChange={(label) => onLabelChange(tab.tabKey, label)}
                    onQueryChange={(query) => onQueryChange(tab.tabKey, query)}
                  />
                </TabPanel>
              ))}
            </TabPanels>
          </Tabs>
        </Stack>
      </Box>
    </Flex>
  );
};
