import type { LinkProps as ChakraLinkProps } from '@chakra-ui/react';
import { Link as ChakraLink } from '@chakra-ui/react';
import type { LinkProps as RouterLinkProps } from 'react-router-dom';
import { Link as RouterLink } from 'react-router-dom';

interface Props {
  href?: string;
  to?: string;
  children?: JSX.Element | string;
  underline?: boolean;
  subtle?: boolean;
}

export const Link = ({
  href,
  to,
  children,
  subtle = false,
  underline = true,
  ...linkProps
}: Props & Partial<ChakraLinkProps> & Partial<RouterLinkProps>) => {
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

  // Panic!
  return <>children</>;
};
