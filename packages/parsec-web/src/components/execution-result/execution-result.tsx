import type { FlexProps } from '@chakra-ui/react';
import {
  Alert,
  AlertDescription,
  AlertIcon,
  Flex,
  Tab,
  TabList,
  TabPanel,
  TabPanels,
  Tabs,
  VStack
} from '@chakra-ui/react';
import type { ResizableProps } from 're-resizable';

import type { ExecutionResultDataSet } from '../../types/parsec';
import { Code } from '../code/code';

import { ExecutionResultDataSetView } from './execution-result-dataset-view';

export type ExecutionResultProps = {
  results: any;
} & FlexProps &
  ResizableProps;

export const ExecutionResult = ({ results, ...props }: ExecutionResultProps) => {
  return (
    <Flex flexDirection="column" align="stretch" overflow="hidden" {...props}>
      {results && (
        <Tabs variant="parsec" flexGrow={1} display="flex" flexDirection="column" overflow="hidden">
          <TabList p="0.5rem" ml="0.5rem">
            <Tab>Data</Tab>
            <Tab>Query</Tab>
            <Tab>Raw</Tab>
          </TabList>
          <TabPanels as={Flex} flexDirection="column" flexGrow={1} align="stretch" overflow="auto">
            <TabPanel flexGrow={1} overflow="scroll">
              <VStack spacing="1rem" align="stretch">
                {results.errors.map((error: string, index: number) => (
                  <Alert status="error" key={index}>
                    <AlertIcon />
                    <AlertDescription>{error}</AlertDescription>
                  </Alert>
                ))}
                {results.dataSets.map((dataSet: ExecutionResultDataSet) => (
                  <ExecutionResultDataSetView key={dataSet.name} dataSet={dataSet} />
                ))}
              </VStack>
            </TabPanel>
            <TabPanel>
              <Code language="clojure">{results?.prettyParsedTree}</Code>
            </TabPanel>
            <TabPanel>
              <Code language="json">{JSON.stringify(results, undefined, 2)}</Code>
            </TabPanel>
          </TabPanels>
        </Tabs>
      )}
    </Flex>
  );
};
