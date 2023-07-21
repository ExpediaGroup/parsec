import { Box, forwardRef, IconButton as ChakraIconButton, Tooltip } from '@chakra-ui/react';

import type { IconFactoryKeys } from '../../shared/icon-factory';
import { iconFactoryAs } from '../../shared/icon-factory';

export interface IconButtonProps {
  bg?: string;
  color?: string;
  icon: IconFactoryKeys;
  fontSize?: string;
  onClick?: () => void;
  size?: string;
  tooltip: string;
  tooltipPlacement?: 'top' | 'right' | 'bottom' | 'left';
  variant?: string;
}

export const IconButton = forwardRef(({ icon, tooltip, tooltipPlacement, ...props }, ref) => {
  return (
    <Box ref={ref}>
      <Tooltip placement={tooltipPlacement} label={tooltip}>
        <ChakraIconButton icon={iconFactoryAs(icon, { 'aria-label': tooltip })} aria-label={tooltip} {...props} />
      </Tooltip>
    </Box>
  );
});
