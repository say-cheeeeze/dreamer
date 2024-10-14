function isEmpty( str ) {
	return null === str || undefined === str || '' === str || 'null' === str || [] === str || str.length === 0;
}

function isNotEmpty( str ) {
	return !isEmpty( str );
}

const CommonJs = {
	isEmpty, isNotEmpty
}
export default CommonJs;