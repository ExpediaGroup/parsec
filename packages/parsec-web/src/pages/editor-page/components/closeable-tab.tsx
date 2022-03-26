import { Box, Button, IconButton, useColorModeValue, useMultiStyleConfig, useTab } from '@chakra-ui/react';
import type { ReactNode } from 'react';
import React from 'react';

import { iconFactoryAs } from '../../../shared/icon-factory';

interface Props {
  key: string;
  children: ReactNode;
  onClose: () => void;
}

export const ClosableTab = React.forwardRef<HTMLElement, Props>((props, ref) => {
  const tabProps = useTab({ ...props, ref });
  const styles = useMultiStyleConfig('Tabs', tabProps);

  return (
    <Button
      as={Box}
      __css={styles.tab}
      display="flex"
      alignItems="center"
      _selected={{ color: useColorModeValue('parsec.pink', 'parsec.blue'), fontWeight: 'bold' }}
      _focus={{ boxShadow: 'none' }}
      {...tabProps}
    >
      {tabProps.children}

      <IconButton
        as={Box}
        icon={iconFactoryAs('close')}
        variant="ghost"
        cursor="pointer"
        size="xss"
        ml="1rem"
        aria-label="Close tab"
        onClick={props.onClose}
        color={useColorModeValue('parsec.pink', 'parsec.blue')}
        _hover={{
          color: useColorModeValue('parsec.blue', 'parsec.pink')
        }}
      />
    </Button>
  );
});
