import { Box, IconButton, Icon, Menu, MenuButton, MenuList, Tooltip, forwardRef } from '@chakra-ui/react';
import type { ReactNode } from 'react';

import { iconFactory } from '../../shared/icon-factory';

export interface IconButtonMenuProps {
  'aria-label': string;
  bg?: string;
  color?: string;
  children: ReactNode;
  icon: string;
  fontSize?: string;
  size?: string;
  tooltip: string;
  variant?: string;
}

const TooltipIcon = forwardRef(
  ({ bg, color, children, fontSize, icon, label, size, tooltip, variant, ...props }, ref) => {
    return (
      <Box ref={ref} {...props}>
        <Tooltip label={tooltip} aria-label={label}>
          <IconButton
            icon={<Icon as={iconFactory(icon)} aria-label={label} />}
            bg={bg}
            color={color}
            fontSize={fontSize}
            size={size}
            variant={variant}
            aria-label={label}
          >
            {children}
          </IconButton>
        </Tooltip>
      </Box>
    );
  }
);

/**
 * IconButton that opens a menu on click.
 */
export const IconButtonMenu = ({
  'aria-label': ariaLabel,
  bg,
  color,
  children,
  icon,
  fontSize,
  size,
  tooltip,
  variant
}: IconButtonMenuProps) => {
  return (
    <Menu>
      <MenuButton
        as={TooltipIcon}
        bg={bg}
        color={color}
        fontSize={fontSize}
        icon={icon}
        label={ariaLabel}
        size={size}
        tooltip={tooltip}
        variant={variant}
      ></MenuButton>
      <MenuList zIndex="10">{children}</MenuList>
    </Menu>
  );
};
