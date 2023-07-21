import { Badge, Button, Code, Heading, HStack, Text, VStack, Wrap } from '@chakra-ui/react';
import { useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { iconFactoryAs } from '../../shared/icon-factory';
import { referenceSlice } from '../../store/reference.slice';
import type { AppDispatch } from '../../store/store';
import { FancyCode } from '../../ui/fancy-code/fancy-code';
import { Link } from '../../ui/link/link';
import { ReferenceToken } from '../reference-token/reference-token';

interface ReferenceDetailProps {
  tokens?: any[];
}

export const ReferenceDetail = ({ tokens }: ReferenceDetailProps) => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  const { key } = useParams();

  if (!tokens) {
    return <></>;
  }

  const setSearch = (search: string) => {
    dispatch(referenceSlice.actions.clearTokenType());
    dispatch(referenceSlice.actions.setSearch(search));
    navigate('/reference');
  };

  const token = tokens.find((token: any) => token.key === key);

  if (!token) {
    return <></>;
  }

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
          <Badge cursor="pointer" onClick={() => setSearch(`type:${token.type}`)}>
            {token.type}
          </Badge>{' '}
          <Badge cursor="pointer" onClick={() => setSearch(`subtype:${token.subtype}`)}>
            {token.subtype}
          </Badge>
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
            <Code key={i}>{syntax}</Code>
          ))}
        </>
      )}

      {token.examples && (
        <>
          <Heading as="h3" size="lg" pt="1rem">
            Examples
          </Heading>
          {token.examples.map((example: any, i: number) => (
            <FancyCode language="parsec" key={i}>
              {example.q}
            </FancyCode>
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
