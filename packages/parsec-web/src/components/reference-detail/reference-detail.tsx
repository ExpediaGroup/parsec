import { Badge, Button, Heading, HStack, Text, VStack, Wrap } from '@chakra-ui/react';
import { useParams } from 'react-router-dom';

import { iconFactoryAs } from '../../shared/icon-factory';
import { Link } from '../../ui/link/link';
import { ReferenceToken } from '../reference-token/reference-token';

interface ReferenceDetailProps {
  tokens?: any[];
}

export const ReferenceDetail = ({ tokens }: ReferenceDetailProps) => {
  const { key } = useParams();

  if (!tokens) {
    return <></>;
  }

  const token = tokens.find((token: any) => token.key === key);

  console.log('token', token);

  return (
    <VStack spacing="1rem" align="stretch">
      <Link to=".." subtle={true} alignSelf="stretch" p="0.5rem">
        <Button variant="link" leftIcon={iconFactoryAs('chevronLeft')} width="full">
          Back to list
        </Button>
      </Link>
      <VStack align="flex-start">
        <ReferenceToken token={token} link={false} size="lg" />
        <HStack>
          <Badge>{token.type}</Badge> <Badge>{token.subtype}</Badge>
        </HStack>
      </VStack>

      {token.description && (
        <>
          {token.description.map((row: any, i: number) => (
            <Text key={i}>{row}</Text>
          ))}
        </>
      )}

      {token.syntax && (
        <>
          <Heading as="h3" size="lg" pt="1rem">
            Syntax
          </Heading>
          {token.syntax.map((syntax: any, i: number) => (
            <Text key={i}>{syntax}</Text>
          ))}
        </>
      )}

      {token.examples && (
        <>
          <Heading as="h3" size="lg" pt="1rem">
            Examples
          </Heading>
          {token.examples.map((example: any, i: number) => (
            <Text key={i}>{example.q}</Text>
          ))}
        </>
      )}

      {token.aliases && (
        <>
          <Heading as="h3" size="lg" pt="1rem">
            Aliases
          </Heading>

          <Wrap shouldWrapChildren={true}>
            {token.aliases.map((key: string) => {
              const aliasToken = tokens.find((token) => token.key === key);
              if (aliasToken) return <ReferenceToken key={key} token={aliasToken} />;
            })}
          </Wrap>
        </>
      )}

      {token.related && (
        <>
          <Heading as="h3" size="lg" pt="1rem">
            See Also
          </Heading>

          <Wrap shouldWrapChildren={true}>
            {token.related.map((key: string) => {
              const relatedToken = tokens.find((token) => token.key === key);
              if (relatedToken) return <ReferenceToken key={key} token={relatedToken} />;
            })}
          </Wrap>
        </>
      )}
    </VStack>
  );
};
