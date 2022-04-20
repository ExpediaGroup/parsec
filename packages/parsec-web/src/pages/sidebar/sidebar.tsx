import type { FlexProps } from '@chakra-ui/react';
import { Flex } from '@chakra-ui/react';
import type { ResizableProps } from 're-resizable';
import { Routes, Route } from 'react-router-dom';

import { QueriesPage } from '../queries-page/queries-page';
import { ReferencePage } from '../reference-page/reference-page';
import { SettingsPage } from '../settings-page/settings-page';

export const Sidebar = ({ ...flexProps }: FlexProps & ResizableProps) => {
  return (
    <Flex flexDirection="column" overflow="auto" {...flexProps}>
      <Routes>
        <Route path="/queries" element={<QueriesPage />} />
        <Route path="/reference" element={<ReferencePage />} />
        <Route path="/settings" element={<SettingsPage />} />
      </Routes>
    </Flex>
  );
};
