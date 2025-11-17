import { dateFnsLocalizer } from 'react-big-calendar';
import {
    parse,
    startOfWeek,
    getDay,
    format,
} from 'date-fns';
import { enUS } from 'date-fns/locale';

// todo determine locale dynamically
const locales = {
    'en-US': enUS,
};

export const localizer = dateFnsLocalizer({
    format,
    parse,
    startOfWeek: () => startOfWeek(new Date(), { weekStartsOn: 1 }), // Monday
    getDay,
    locales,
});
