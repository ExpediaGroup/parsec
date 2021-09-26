import { Icon } from '@chakra-ui/react';
import type { ReactElement } from 'react';
import type { IconType } from 'react-icons';
import { BsFillMoonStarsFill, BsFillSunFill } from 'react-icons/bs';
import { GoMarkGithub } from 'react-icons/go';
import { GrStatusUnknown } from 'react-icons/gr';

const icons = {
  github: GoMarkGithub,
  darkMode: BsFillMoonStarsFill,
  lightMode: BsFillSunFill,
  unknown: GrStatusUnknown
};

type IconFactory = typeof icons;
type IconFactoryKeys = keyof IconFactory;

export const iconFactory = (key: IconFactoryKeys): IconType => {
  if (icons[key]) {
    return icons[key];
  }

  // Fallback!
  console.error(`[ICON_FACTORY] Missing icon for ${key}!`);
  return GrStatusUnknown;
};

export const iconFactoryAs = (key: IconFactoryKeys, props = {}): ReactElement => {
  return <Icon as={iconFactory(key)} {...props} />;
};
