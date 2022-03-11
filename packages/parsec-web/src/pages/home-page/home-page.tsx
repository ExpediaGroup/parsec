import type { FlexProps } from '@chakra-ui/react';
import { useColorModeValue } from '@chakra-ui/react';
import { Button, Flex, Heading, Image, Stack, Text, VStack } from '@chakra-ui/react';

import gridUrl from '../../../assets/grid.svg';
import parsecLogoUrl from '../../../assets/parsec-logo.svg';
import { Link } from '../../components/link/link';

interface Props {
  innerProps?: FlexProps;
  outerProps?: FlexProps;
  children?: JSX.Element;
}
const Section = ({ children, innerProps, outerProps }: Props) => (
  <Flex direction="row" minHeight="80vh" align="center" justify="center" {...outerProps}>
    <Flex direction="column" align="stretch" maxWidth="75rem" flexGrow={1} {...innerProps}>
      {children}
    </Flex>
  </Flex>
);

export const HomePage = () => {
  return (
    <>
      <Flex direction="column" minHeight="60vh" align="stretch">
        <Section>
          <Flex direction={{ base: 'column-reverse', md: 'row' }} align="center" justify="space-evenly">
            <VStack>
              <Heading fontSize={{ base: '2xl', sm: '2xl', md: '4xl', xl: '6xl' }}>Go the Distance With</Heading>
              <Heading fontSize={{ base: '6xl', sm: '7xl', md: '8xl', xl: '9xl' }}>Parsec</Heading>

              <Heading
                pt={{ base: 0, md: '4rem' }}
                fontSize={{ base: 'sm', sm: 'md', md: 'lg', xl: '2xl' }}
                color="parsec.fuchsia"
              >
                A query engine you'll enjoy using
              </Heading>

              <Stack direction={{ base: 'column', md: 'row' }} spacing="2rem" pt="3rem">
                <Link to="/editor" subtle={true}>
                  <Button variant="fancy">Get Started</Button>
                </Link>

                <Link to="/quickstart" subtle={true}>
                  <Button variant="fancy">Learn More</Button>
                </Link>
              </Stack>
            </VStack>
            <Image
              src={parsecLogoUrl}
              alt="Parsec Logo"
              boxSize={{ base: '16rem', sm: '20rem', md: '24rem', xl: '28rem' }}
            />
          </Flex>
        </Section>

        <Section
          outerProps={{
            _light: {
              bg: 'gray.100'
            },
            _dark: {
              bg: 'gray.900'
            },
            //bgImage: `url(${gridUrl})`,
            bgSize: 'contain',
            bgRepeat: 'no-repeat',
            bgPosition: 'center bottom'
            //bgGradient: 'linear(gray.900 0%, parsec.pink 100%)'
          }}
          //innerProps={{ bg: 'gray.700', borderRadius: '2rem', p: '2rem', minH: '25rem' }}
        >
          <VStack
            spacing="2rem"
            fontSize="xl"
            margin="auto"
            px={{ base: 0, lg: '10rem' }}
            maxW={{ base: '90vw', lg: '60vw' }}
          >
            <Heading fontSize="6xl" pb="6rem" textAlign="center" color={useColorModeValue('black', 'white')}>
              A versatile query engine for a complex world
            </Heading>
            <Text color={useColorModeValue('parsec.purple', 'parsec.yellow')}>
              <Text as="span" textStyle="parsec">
                Parsec
              </Text>{' '}
              is a data processing and calculation engine for running analytic queries against various data sources.
            </Text>
            <Text color="parsec.pink">
              Queries are written in a domain-specific language (DSL) which has support for loading, filtering,
              transforming, and analyzing data from various input sources.
            </Text>
            <Text color="parsec.blue">
              <Text as="span" textStyle="parsec">
                Parsec
              </Text>{' '}
              is focused on dataset operations, but also has support for variables and custom functions.
            </Text>
          </VStack>
        </Section>

        <Section
          outerProps={{
            //bgImage: `url(${gridUrl})`,
            bgSize: 'contain',
            bgRepeat: 'no-repeat',
            bgPosition: 'center bottom'
            //bgGradient: 'linear(gray.900 0%, parsec.pink 100%)'
          }}
          // nnerProps={{ bg: 'gray.700', borderRadius: '2rem', p: '4rem', minH: '25rem' }}
        >
          <VStack
            spacing="2rem"
            fontSize="xl"
            margin="auto"
            px={{ base: 0, lg: '10rem' }}
            maxW={{ base: '90vw', lg: '60vw' }}
          >
            <Heading fontSize="6xl" pb="3rem" textAlign="center">
              Why Parsec?
            </Heading>
            <Text>
              <Text as="span" textStyle="white">
                Parsec
              </Text>{' '}
              is a lightweight solution for small-to-medium data queries. It can pull data from disparate sources
              together for combined correlation or aggregation.
            </Text>
            <Text>
              As a JVM-compatible library,{' '}
              <Text as="span" textStyle="parsec">
                Parsec
              </Text>{' '}
              can be integrated in other applications which need the flexibility to execute dynamic queries, leveraging{' '}
              <Text as="span" textStyle="parsec">
                Parsec
              </Text>
              's rich and fully-tested feature set without re-inventing the wheel. And as a web service, it can easily
              be leveraged by websites or other services.
            </Text>
          </VStack>
        </Section>
      </Flex>
    </>
  );
};
