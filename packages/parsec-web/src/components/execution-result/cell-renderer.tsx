import { Badge, Text, useColorModeValue } from '@chakra-ui/react';
import isArray from 'lodash/isArray';
import isBoolean from 'lodash/isBoolean';
import isNull from 'lodash/isNull';
import isObjectLike from 'lodash/isObjectLike';

export const CellRenderer = (value: any) => {
  const accentColor = useColorModeValue('parsec.orange', 'parsec.blue');

  if (isNull(value)) {
    return (
      <Badge bg={accentColor} color="black">
        null
      </Badge>
    );
  }
  if (isBoolean(value)) {
    return <Text>{Boolean(value).toString()}</Text>;
  }
  if (isObjectLike(value)) {
    return <Text>{JSON.stringify(value)}</Text>;
  }
  if (isArray(value)) {
    return <Text>[{value.map((v) => CellRenderer(v)).join(', ')}]</Text>;
  }

  return <Text>{JSON.stringify(value)}</Text>;
};
