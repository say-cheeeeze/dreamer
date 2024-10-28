function isEmpty( param ) {
	return null === param || undefined === param || '' === param || 'null' === param || [] === param || param.length === 0;
}

function isNotEmpty( param ) {
	return !isEmpty( param );
}

const CommonJs = {
	isEmpty, isNotEmpty
}
export default CommonJs;