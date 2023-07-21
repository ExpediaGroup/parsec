import type { LinkProps as ChakraLinkProps } from '@chakra-ui/react';
import { Link as ChakraLink } from '@chakra-ui/react';
import type { LinkProps as RouterLinkProps } from 'react-router-dom';
import { Link as RouterLink } from 'react-router-dom';

export type LinkProps = {
  href?: string;
  to?: string;
  onClick?: () => void;
  children?: JSX.Element | string;
  underline?: boolean;
  subtle?: boolean;
} & Partial<ChakraLinkProps> &
  Partial<RouterLinkProps>;

/**
 * Link component that can be used to navigate to a different page or to an external URL.
 */
export const Link = ({ href, onClick, to, children, subtle = false, underline = true, ...linkProps }: LinkProps) => {
  const extraProps = {
    ...(!underline || subtle ? { _hover: { ...linkProps._hover, textDecoration: 'none' } } : {}),
    ...(subtle ? { _focus: { ...linkProps._focus, boxShadow: 'unset' } } : {})
  };

  if (href != undefined) {
    return (
      <ChakraLink href={href} isExternal={true} {...linkProps} {...extraProps}>
        {children}
      </ChakraLink>
    );
  }
  if (to != undefined) {
    return (
      <ChakraLink as={RouterLink} to={to} {...linkProps} {...extraProps}>
        {children}
      </ChakraLink>
    );
  }
  if (onClick != undefined) {
    return (
      <ChakraLink onClick={onClick} {...linkProps} {...extraProps}>
        {children}
      </ChakraLink>
    );
  }

  // Panic!
  return <>children</>;
};
