import { HStack, Heading } from '@chakra-ui/react';

import { Link } from '../link/link';
import { TokenIcon } from '../token-icon/token-icon';

interface ReferenceTokenProps {
  link?: boolean;
  token: any;
  size?: string;
}

export const ReferenceToken = ({ link = true, size = 'md', token }: ReferenceTokenProps) => {
  const component = (
    <HStack spacing="0.5rem" align="center">
      <TokenIcon type={token.type} />
      <Heading as="span" size={size} p={0} m={0}>
        {token.name} {token.altName && <span>({token.altName})</span>}
      </Heading>
    </HStack>
  );

  return link ? (
    <Link to={`/reference/${token.key}`} key={token.key}>
      {component}
    </Link>
  ) : (
    component
  );
};
