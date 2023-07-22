import type { FlexProps } from '@chakra-ui/react';
import { Alert, AlertDescription, AlertIcon } from '@chakra-ui/react';
import { Flex } from '@chakra-ui/react';
import { Resizable, type ResizableProps } from 're-resizable';
import { ErrorBoundary } from 'react-error-boundary';
import { Routes, Route } from 'react-router-dom';

import { HistoryPage } from '../history-page/history-page';
import { QueriesPage } from '../queries-page/queries-page';
import { ReferencePage } from '../reference-page/reference-page';
import { SettingsPage } from '../settings-page/settings-page';

export type SidebarProps = Omit<FlexProps, 'onResize'> & ResizableProps;

export const Sidebar = ({ ...flexProps }: SidebarProps) => {
  return (
    <Flex as={Resizable} flexDirection="column" overflow="auto" align="stretch" {...flexProps as any}>
      <ErrorBoundary
        FallbackComponent={({ error }) => {
          return (
            <Alert status="error">
              <AlertIcon />
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          );
        }}
      >
        <Routes>
          <Route path="/queries" element={<QueriesPage />} />
          <Route path="/history" element={<HistoryPage />} />
          <Route path="/reference/*" element={<ReferencePage />} />
          <Route path="/settings" element={<SettingsPage />} />
        </Routes>
      </ErrorBoundary>
    </Flex>
  );
};
