import { HStack } from '@chakra-ui/react';

import type { TokenType } from '../../../../types/parsec';
import { TokenIcon } from '../../../token-icon/token-icon';

export interface TokenTypeFilterProps {
  onTokenType: (tokenType: TokenType) => void;
  tokenType?: TokenType;
}

const tokenTypes = ['input', 'literal', 'operator', 'statement', 'symbol', 'function'];

export const TokenTypeFilter = ({ onTokenType, tokenType }: TokenTypeFilterProps) => {
  return (
    <HStack spacing="0.5rem" justify="space-evenly">
      {tokenTypes.map((type) => (
        <TokenIcon
          type={type}
          key={type}
          onClick={() => onTokenType(type as TokenType)}
          isDisabled={tokenType === undefined ? false : type !== tokenType}
        />
      ))}
    </HStack>
  );
};
