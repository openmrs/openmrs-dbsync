import {ConflictAction, ConflictActionType} from "./conflict.actions";
import {createFeatureSelector, createSelector} from "@ngrx/store";
import {Conflict} from "../conflict";
import {ConflictCountAndItems} from "../confict-count-and-items";

export interface ConflictState {
	countAndItems: ConflictCountAndItems;
	conflictToView?: Conflict;
}

const GET_CONFLICTS_FEATURE_STATE = createFeatureSelector<ConflictState>('conflictQueue');

export const GET_CONFLICTS = createSelector(
	GET_CONFLICTS_FEATURE_STATE,
	state => state.countAndItems
);

export const CONFLICT_TO_VIEW = createSelector(
	GET_CONFLICTS_FEATURE_STATE,
	state => state.conflictToView
);

const initialState: ConflictState = {
	countAndItems: new ConflictCountAndItems()
};

export function conflictReducer(state = initialState, action: ConflictAction) {

	switch (action.type) {

		case ConflictActionType.CONFLICTS_LOADED:
			return {
				...state,
				countAndItems: action.countAndItems
			};

		case ConflictActionType.VIEW_CONFLICT:
			return {
				...state,
				conflictToView: action.conflictToView
			};

		default:
			return state;
	}

}
