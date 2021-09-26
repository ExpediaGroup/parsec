import { Text, VStack } from '@chakra-ui/react';

import { Editor } from '../../components/editor/editor';

export const EditorPage = () => {
  return (
    <VStack p="1rem">
      <Text>The Editor is the cornerstone of this application.</Text>

      <Editor contents="import mock" />
    </VStack>
  );
};
