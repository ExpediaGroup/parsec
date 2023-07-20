import type { BoxProps } from '@chakra-ui/react';
import { Alert, AlertDescription, AlertIcon, Box } from '@chakra-ui/react';
import type { ReactNode } from 'react';

import { Spinner } from '../spinner/spinner';

export interface LoadingContainerProps {
  children: ReactNode;
  error?: any;
  errorProps?: Partial<BoxProps>;
  isLoading: boolean;
  loadingProps?: Partial<BoxProps>;
}

/**
 * Wrapper component that displays a loading spinner or error message if any,
 * else it renders the children.
 */
export const LoadingContainer = ({ children, error, errorProps, isLoading, loadingProps }: LoadingContainerProps) => {
  if (error) {
    return (
      <Box {...errorProps}>
        <Alert status="error">
          <AlertIcon />
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      </Box>
    );
  }

  if (isLoading) {
    return (
      <Box alignSelf="center" {...loadingProps}>
        <Spinner />
      </Box>
    );
  }

  return <>{children}</>;
};
