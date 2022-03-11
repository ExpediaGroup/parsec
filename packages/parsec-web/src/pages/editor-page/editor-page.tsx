import { Box, Flex, TabList, TabPanel, TabPanels, Tabs, Text, VStack } from '@chakra-ui/react';
import { useState } from 'react';

import { Editor } from '../../components/editor/editor';

export const EditorPage = () => {
  // Selected Tab
  // The first tab (0) always contains Insight Metadata
  //const [tabIndex, setTabIndex] = useState(0);

  return (
    // Fill remaining space in page between header/footer
    // This fixes footer at the bottom of the screen
    <Flex direction="row" flexGrow={1} overflow="hidden" position="relative">
      {/* Create a scrolling container inside the page */}
      <Box position="absolute" top={0} bottom={0} left={0} right={0} overflow="auto">
        <VStack p="1rem">
          {/* <Tabs
            index={tabIndex}
            onChange={(index: number) => setTabIndex(index)}
            flex="5 1 50%"
            overflow="auto"
            borderColor="gray.300"
            borderWidth="1px"
            borderTopWidth={0}
            borderBottomRightRadius="lg"
          >
            <TabList></TabList>
            <TabPanels></TabPanels>
          </Tabs> */}

          <Editor contents="import mock" />
        </VStack>
      </Box>
    </Flex>
  );
};
