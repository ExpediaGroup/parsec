import { Box, IconButton, Icon, Menu, MenuButton, MenuList, Tooltip, forwardRef } from '@chakra-ui/react';
import type { ReactNode, ElementType } from 'react';

interface Props {
  'aria-label': string;
  bg?: string;
  color?: string;
  children: ReactNode;
  icon: ElementType;
  fontSize?: string;
  size?: string;
  tooltip: string;
  variant?: string;
}

const TooltipIcon = forwardRef(({ bg, color, children, fontSize, label, size, tooltip, variant, ...props }, ref) => {
  if (bg !== undefined) {
    return (
      <Box ref={ref} {...props}>
        <Tooltip label={tooltip} aria-label={label}>
          <IconButton bg={bg} color={color} fontSize={fontSize} size={size} variant={variant} aria-label={label}>
            {children}
          </IconButton>
        </Tooltip>
      </Box>
    );
  }

  return (
    <Box ref={ref} {...props}>
      <Tooltip label={tooltip} aria-label={label}>
        <IconButton color={color} fontSize={fontSize} size={size} variant={variant} aria-label={label}>
          {children}
        </IconButton>
      </Tooltip>
    </Box>
  );
});

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
}: Props) => {
  return (
    <Menu>
      <MenuButton
        as={TooltipIcon}
        bg={bg}
        color={color}
        fontSize={fontSize}
        label={ariaLabel}
        size={size}
        tooltip={tooltip}
        variant={variant}
      >
        <Icon as={icon} aria-label={ariaLabel} />
      </MenuButton>
      <MenuList zIndex="10">{children}</MenuList>
    </Menu>
  );
};
