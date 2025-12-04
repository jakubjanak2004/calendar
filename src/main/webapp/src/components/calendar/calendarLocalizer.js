import { dateFnsLocalizer } from 'react-big-calendar';
import {
    parse,
    startOfWeek,
    getDay,
    format,
} from 'date-fns';
import { enUS, cs, sk } from 'date-fns/locale';

const locales = {
    'en-US': enUS,
    'cs-CZ': cs,
    'sk-SK': sk,
};

export const localizer = dateFnsLocalizer({
    format,
    parse,
    startOfWeek: () => startOfWeek(new Date(), { weekStartsOn: 1 }), // Monday
    getDay,
    locales,
});
