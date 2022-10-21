import type { FlexProps } from '@chakra-ui/react';
import { Alert, AlertDescription, AlertIcon } from '@chakra-ui/react';
import { Flex } from '@chakra-ui/react';
import type { ResizableProps } from 're-resizable';
import { ErrorBoundary } from 'react-error-boundary';
import { Routes, Route } from 'react-router-dom';

import { QueriesPage } from '../queries-page/queries-page';
import { ReferencePage } from '../reference-page/reference-page';
import { SettingsPage } from '../settings-page/settings-page';

export const Sidebar = ({ ...flexProps }: FlexProps & ResizableProps) => {
  return (
    <Flex flexDirection="column" overflow="auto" align="stretch" {...flexProps}>
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
          <Route path="/reference/*" element={<ReferencePage />} />
          <Route path="/settings" element={<SettingsPage />} />
        </Routes>
      </ErrorBoundary>
    </Flex>
  );
};
