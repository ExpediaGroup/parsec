import { Box, Button, IconButton, Spinner, useColorModeValue, useMultiStyleConfig, useTab } from '@chakra-ui/react';
import type { ReactNode } from 'react';
import React from 'react';

import { iconFactoryAs } from '../../../shared/icon-factory';

interface Props {
  key: string;
  children: ReactNode;
  isLoading?: boolean;
  onClose: () => void;
}

export const ClosableTab = React.forwardRef<HTMLElement, Props>((props, ref) => {
  const { isLoading, onClose, ...rest } = props;
  const tabProps = useTab({ ...rest, ref });
  const styles = useMultiStyleConfig('Tabs', tabProps);

  return (
    <Button
      as={Box}
      __css={styles.tab}
      userSelect="none"
      display="flex"
      alignItems="center"
      _selected={{
        color: useColorModeValue('parsec.pink', 'parsec.blue'),
        fontWeight: '800'
      }}
      _focus={{ boxShadow: 'none' }}
      {...tabProps}
    >
      {isLoading === true && (
        <Spinner thickness="2px" speed="0.65s" emptyColor="gray.200" color="parsec.blue" size="md" mr="0.5rem" />
      )}

      {tabProps.children}

      <IconButton
        as={Box}
        icon={iconFactoryAs('close')}
        variant="ghost"
        cursor="pointer"
        size="xss"
        ml="1rem"
        aria-label="Close tab"
        onClick={onClose}
        color="gray.300"
        _hover={{
          color: useColorModeValue('parsec.blue', 'parsec.pink')
        }}
      />
    </Button>
  );
});
