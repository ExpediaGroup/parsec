import type { IconProps } from '@chakra-ui/react';
import { Flex, Icon, Tooltip } from '@chakra-ui/react';

import { iconFactory } from '../../shared/icon-factory';

export interface TokenIconProps {
  type: string;
}

export const TokenIcon = ({ type, ...props }: TokenIconProps & IconProps) => {
  return (
    <Tooltip label={`Token type: ${type}`} aria-label={`Token type: ${type}`}>
      <Flex flexDirection="column" align="center">
        <Icon as={iconFactory('circle')} color={`parsec.tokens.${type}`} {...props} />
      </Flex>
    </Tooltip>
  );
};
