import type { FlexProps } from '@chakra-ui/react';
import { Box, Flex, Icon, Stack, Tab, TabList, TabPanel, TabPanels, Tabs, useColorModeValue } from '@chakra-ui/react';
import { useDispatch, useSelector } from 'react-redux';

import { QueryTab } from '../../components/query-tab/query-tab';
import { iconFactory } from '../../shared/icon-factory';
import type { QueryTabState } from '../../store/editor.slice';
import { editorSlice, isHomeTab, isQueryTab } from '../../store/editor.slice';
import type { RootState } from '../../store/store';
import { HomePage } from '../home-page/home-page';

import { ClosableTab } from './components/closeable-tab';

export const EditorPage = (flexProps: FlexProps) => {
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

  const onQueryTabChange = (tabKey: string, rest: Partial<QueryTabState>) => {
    dispatch(editorSlice.actions.updateQueryTab({ tabKey, ...rest }));
  };

  const onNewTab = () => {
    dispatch(editorSlice.actions.openNewQueryTab());
  };

  const onCloseTab = (tabKey: string) => {
    dispatch(editorSlice.actions.closeTab({ tabKey }));
  };

  return (
    // Fill remaining space in page between header/footer
    // This fixes footer at the bottom of the screen
    <Flex direction="row" overflow="hidden" position="relative" {...flexProps}>
      {/* Create a scrolling container inside the page */}
      <Box position="absolute" top={0} bottom={0} left={0} right={0} overflow="hidden">
        <Stack direction={{ base: 'column', md: 'row' }} align="stretch" height="100%">
          {/* <ActivityBar />
          <Sidebar flex="1 1 20%" maxWidth="28rem" /> */}

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
              flexShrink={0}
              sx={{
                scrollbarWidth: 'thin'
              }}
            >
              {tabs.map((tab) => (
                <ClosableTab
                  key={tab.tabKey}
                  isLoading={isQueryTab(tab) ? tab.isRunning : false}
                  onClose={() => onCloseTab(tab.tabKey)}
                >
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
                <TabPanel key={tab.tabKey} flexGrow={1} display="flex" flexDirection="column" overflow="auto" p={0}>
                  {isHomeTab(tab) && <HomePage />}

                  {isQueryTab(tab) && (
                    <QueryTab
                      tab={tab}
                      query={tab.query}
                      onLabelChange={(label) => onLabelChange(tab.tabKey, label)}
                      onRunningChange={(isRunning: boolean) => onQueryTabChange(tab.tabKey, { isRunning })}
                      onQueryChange={(query) => onQueryChange(tab.tabKey, query)}
                      flexGrow={1}
                    />
                  )}
                </TabPanel>
              ))}
            </TabPanels>
          </Tabs>
        </Stack>
      </Box>
    </Flex>
  );
};
