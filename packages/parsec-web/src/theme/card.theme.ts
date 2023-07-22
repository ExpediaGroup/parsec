import { cardAnatomy } from '@chakra-ui/anatomy'
import { createMultiStyleConfigHelpers } from '@chakra-ui/react'

const { definePartsStyle, defineMultiStyleConfig } =
  createMultiStyleConfigHelpers(cardAnatomy.keys)

const baseStyle = definePartsStyle({
  // define the part you're going to style
  container: {
    borderRadius: 'lg',
    boxShadow: 'unset',
    bg: 'gray.100',
    _dark: {
      bg: 'gray.700'
    }
  }
})

export const cardTheme = defineMultiStyleConfig({ baseStyle })