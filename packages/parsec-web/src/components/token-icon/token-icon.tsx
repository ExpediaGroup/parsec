import type { IconProps } from '@chakra-ui/react';
import { Flex, Icon, Tooltip } from '@chakra-ui/react';

import { iconFactory } from '../../shared/icon-factory';

export type TokenIconProps = {
  type: string;
  isDisabled?: boolean;
} & IconProps;

export const TokenIcon = ({ isDisabled = false, type, ...props }: TokenIconProps) => {
  return (
    <Tooltip label={`Token type: ${type}`} aria-label={`Token type: ${type}`}>
      <Flex flexDirection="column" align="center">
        <Icon as={iconFactory('circle')} color={`parsec.tokens.${type}`} opacity={isDisabled ? 0.5 : 1} {...props} />
      </Flex>
    </Tooltip>
  );
};
